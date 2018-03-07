### QueryStream: A simpler way to build JPA Criteria queries, using a Stream-like API

QueryStream allows you to perform JPA queries using a `Stream`-like API.

Just like Java 8's `Stream`s `QueryStream`s are built up in a pipeline, using methods like `map()`, `flatMap()`, `filter()`, etc.

With `QueryStream`, each step in the pipeline applies configuration to an internally generated [JPA Criteria query](https://docs.oracle.com/javaee/7/api/?javax/persistence/criteria/package-summary.html).

When you're ready to execute the pipeline:

  * Invoke `QueryStream.toCriteriaQuery()` to create a [CriteriaQuery](https://docs.oracle.com/javaee/7/api/?javax/persistence/CriteriaQuery.html)
  * Invoke `QueryStream.toQuery()` to do #1 and also create a [TypedQuery](https://docs.oracle.com/javaee/7/api/?javax/persistence/TypedQuery.html)
  * Invoke `QueryStream.getResultList()` to do #1 and #2 also execute the query

## Example

Payroll costs are getting out of control. You need a list of all managers whose direct reports have a average salary above $50,000, ordered from highest to lowest.

Here's how you'd build a Criteria query the usual way:

```java
    @PersistenceContext
    private EntityManager entityManager;
    private CriteriaBuilder cb;

    @PostConstruct
    private void setupCriteriaBuilder() {
        this.cb = this.entityManager.getCriteriaBuilder();
    }

    public List<Employee> getHighPayrollManagers(EntityManager entityManager) {

        // Construct query
        final CriteriaQuery criteriaQuery = cb.createQuery();
        final Root<Employee> manager = criteriaQuery.from(Employee.class);
        final SetJoin<Employee, Employee> directReports = manager.join(Employee_.directReports);
        final Expression<Double> avgSalary = cb.avg(directReports.get(Employee_.salary));
        criteriaQuery.where(cb.greaterThan(avgSalary, 50000.0))
        criteriaQuery.groupBy(manager);
        criteriaQuery.orderBy(avgSalary);

        // Execute query
        final TypedQuery<Employee> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }
```

With QueryStream, your code becomes more visually intuitive:

```java
    @PersistenceContext
    private EntityManager entityManager;
    private CriteriaBuilder cb;
    private QueryStream.Builder qb;

    @PostConstruct
    private void setupBuilders() {
        this.cb = this.entityManager.getCriteriaBuilder();
        this.qb = QueryStream.newBuilder(this.entityManager);
    }

    public List<Employee> getHighPayrollManagers() {

        // Create references we will use
        final RootRef<Employee> manager = new RootRef<>();
        final ExprRef<Double> avgSalary = new ExprRef<>();

        // Build and execute query
        return qb.stream(Employee.class)
          .bind(manager)
          .flatMap(Employee_.directReports)
          .mapToDouble(Employee_.salary)
          .average()
          .filter(v -> cb.greaterThan(v, 50000))
          .bind(avgSalary)
          .groupBy(manager)
          .orderBy(avgSalary, false)
          .getResultList();
    }
```

## Bulk Updates and Deletes

Bulk updates and deletes are also supported.

The [QueryStream](http://archiecobbs.github.io/querystream/site/apidocs/index.html?org/dellroad/querystream/jpa/QueryStream) interface has three subinterfaces for searching, bulk updates, and bulk deletes; these are [SearchStream](http://archiecobbs.github.io/querystream/site/apidocs/index.html?org/dellroad/querystream/jpa/SearchStream), [UpdateStream](http://archiecobbs.github.io/querystream/site/apidocs/index.html?org/dellroad/querystream/jpa/UpdateStream), and [DeleteStream](http://archiecobbs.github.io/querystream/site/apidocs/index.html?org/dellroad/querystream/jpa/DeleteStream).

 * A [SearchStream](http://archiecobbs.github.io/querystream/site/apidocs/index.html?org/dellroad/querystream/jpa/SearchStream) builds an internal [CriteriaQuery](https://docs.oracle.com/javaee/7/api/?javax/persistence/CriteriaQuery.html) instance
 * An [UpdateStream](http://archiecobbs.github.io/querystream/site/apidocs/index.html?org/dellroad/querystream/jpa/UpdateStream) builds an internal [CriteriaUpdate](https://docs.oracle.com/javaee/7/api/?javax/persistence/CriteriaUpdate.html) instance
 * A [DeleteStream](http://archiecobbs.github.io/querystream/site/apidocs/index.html?org/dellroad/querystream/jpa/DeleteStream) builds an internal [CriteriaDelete](https://docs.oracle.com/javaee/7/api/?javax/persistence/CriteriaDelete.html) instance

## Single Values

Some queries are known to return a single value. The [SearchValue](http://archiecobbs.github.io/querystream/site/apidocs/index.html?org/dellroad/querystream/jpa/SearchValue) and its subinterfaces have a `value()` method, which executes the query and returns the value:

```java
    public double getAverageSalary(Employee manager) {
        return qb.stream(Employee.class)
          .filter(e -> cb.equal(e, manager))
          .flatMap(Employee_.directReports)
          .mapToDouble(Employee_.salary)
          .average()
          .value();
    }
```

## References

[Ref](http://archiecobbs.github.io/querystream/site/apidocs/index.html?org/dellroad/querystream/jpa/Ref) objects give you a way to refer to items in the stream pipeline at a later step, by `bind()`ing the reference at an earlier step.

References also help code clarity, because they provide a way to give meaningful names to important expressions.

See the `getHighPayrollManagers()` example above for how it works.

## Subqueries

Any stream can be converted into a subquery using `asSubquery()` or `exists()`.

To find all managers who have a direct report making over $100,000:

```java
    public List<Employee> findManagersWithSixFigureDirectReport() {
        return qb.stream(Employee.class)
          .filter(manager -> qb.stream(Employee.class)
                .filter(report -> cb.and(
                    cb.equal(report.get(Employee_.manager), manager),
                    cb.greaterThan(report.get(Employee_.salary), 100000.0)))
                .exists());
    }
```

To find all employees with salary greater than the average of all their manager's direct reports:

```java
    public List<Employee> findEmployeesWithAboveAverageSalaries() {
        return qb.stream(Employee.class)
          .filter(employee -> cb.greaterThan(
              employee.get(Employee_.salary),
              qb.stream(Employee.class)
                .filter(coworker ->
                  builder2.equal(
                    coworker.get(Employee_.manager),
                    employee.get(Employee_.manager)))
                .mapToDouble(Employee_.salary)
                .average()
                .asSubquery()))
          .getResultList();
    }
```

Hmmm, that's a lot of nesting. You can make the code clearer by building the subquery separately, and using a reference for the correlation:

```java
    public DoubleValue getAvgCoworkerSalary(RootRef<Employee> employee) {
        return qb.stream(Employee.class)
          .filter(coworker -> cb.equal(coworker.get(Employee_.manager), employee.get().get(Employee_.manager)))
          .mapToDouble(Employee_.salary)
          .average();
    }

    public List<Employee> findEmployeesWithAboveAverageSalaries() {
        final RootRef<Employee> employee = new RootRef<>();
        return qb.stream(Employee.class)
          .bind(employee)
          .filter(employee ->
            cb.greaterThan(employee.get(Employee_.salary), getAvgCoworkerSalary(employee).asSubquery()))
          .getResultList();
    }
```

## Multiselect and Grouping

To select multiple items, or construct a Java instance, use `mapToSelection()`.

For grouping, use `groupBy()` and `having()`.

Here's an example that finds all managers paired with the average salary of their direct reports, where that average salary is at least $50,000, sorted by average salary descending:

```java
    public List<Object[]> getHighPayrollManagers2() {

        // Create references
        final RootRef<Employee> manager = new RootRef<>();
        final ExprRef<Double> avgSalary = new ExprRef<>();

        // Build and execute stream
        return qb.stream(Employee.class)
          .bind(manager)
          .flatMap(Employee_.directReports)
          .mapToDouble(Employee_.salary)
          .average()
          .bind(avgSalary)
          .groupBy(manager)
          .mapToSelection(Object[].class, e -> cb.array(manager.get(), avgSalary.get()))
          .orderBy(avgSalary, false);
          .having(avgSalary -> cb.gt(avgSalary, 50000.0))
          .getResultList();
    }
```

### Installation

QueryStream is available from [Maven Central](http://search.maven.org/#search|ga|1|a%3Aquerystream-jpa):

```xml
    <dependency>
        <groupId>org.dellroad</groupId>
        <artifactId>querystream-jpa</artifactId>
    </dependency>
```

### API Javadocs

Located [here](http://archiecobbs.github.io/querystream/site/apidocs/index.html?org/dellroad/querystream/jpa/QueryStream.html)
