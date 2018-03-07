### QueryStream

QueryStream allows you to perform JPA queries using a `Stream`-like API.

Just like a Java 8 `Stream`, a `QueryStream` is built up in a pipeline, using methods like `map()`, `flatMap()`, `filter()`, etc.

Each step in a `QueryStream` pipeline modifies an internal [JPA Criteria query](https://docs.oracle.com/javaee/7/api/?javax/persistence/criteria/package-summary.html).

When you're ready to execute the pipeline:

  * Invoke `QueryStream.toCriteriaQuery()` to extract the [CriteriaQuery](https://docs.oracle.com/javaee/7/api/?javax/persistence/criteria/CriteriaQuery.html); or
  * Invoke `QueryStream.toQuery()` to do #1 and also create a [TypedQuery](https://docs.oracle.com/javaee/7/api/?javax/persistence/TypedQuery.html); or
  * Invoke `QueryStream.getResultList()` to do #1 and #2 then execute the query

## Example

Payroll costs are getting out of control. You need a list of all managers whose direct reports have an average salary above $50,000, ordered from highest to lowest.

Here's how you'd build a Criteria query the usual way:

```java
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

    @PersistenceContext
    private EntityManager entityManager;
    private CriteriaBuilder cb;

    @PostConstruct
    private void setupCriteriaBuilder() {
        this.cb = this.entityManager.getCriteriaBuilder();
    }
```

With QueryStream, your code becomes more visually intuitive:

```java
    public List<Employee> getHighPayrollManagers() {

        // Create a couple of references
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

    @PersistenceContext
    private EntityManager entityManager;
    private CriteriaBuilder cb;
    private QueryStream.Builder qb;

    @PostConstruct
    private void setupBuilders() {
        this.cb = this.entityManager.getCriteriaBuilder();
        this.qb = QueryStream.newBuilder(this.entityManager);
    }
```

[See below](#references) for more information about references.

## Bulk Updates and Deletes

Bulk deletes and updates are also supported.

The [QueryStream](http://querystream.github.io/querystream/site/apidocs/index.html?org/dellroad/querystream/jpa/QueryStream.html) interface has three subinterfaces for searches, bulk deletes, and bulk updates; these are [SearchStream](http://querystream.github.io/querystream/site/apidocs/index.html?org/dellroad/querystream/jpa/SearchStream), [DeleteStream](http://querystream.github.io/querystream/site/apidocs/index.html?org/dellroad/querystream/jpa/DeleteStream), and [UpdateStream](http://querystream.github.io/querystream/site/apidocs/index.html?org/dellroad/querystream/jpa/UpdateStream).

 * A [SearchStream](http://querystream.github.io/querystream/site/apidocs/index.html?org/dellroad/querystream/jpa/SearchStream.html) builds an internal [CriteriaQuery](https://docs.oracle.com/javaee/7/api/?javax/persistence/criteria/CriteriaQuery.html) instance
 * A [DeleteStream](http://querystream.github.io/querystream/site/apidocs/index.html?org/dellroad/querystream/jpa/DeleteStream.html) builds an internal [CriteriaDelete](https://docs.oracle.com/javaee/7/api/?javax/persistence/criteria/CriteriaDelete.html) instance
 * An [UpdateStream](http://querystream.github.io/querystream/site/apidocs/index.html?org/dellroad/querystream/jpa/UpdateStream.html) builds an internal [CriteriaUpdate](https://docs.oracle.com/javaee/7/api/?javax/persistence/criteria/CriteriaUpdate.html) instance

## Single Values

Some queries are known to return a single value. The [SearchValue](http://querystream.github.io/querystream/site/apidocs/index.html?org/dellroad/querystream/jpa/SearchValue.html) and its subinterfaces have a `value()` method, which executes the query and returns the value:

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

[Ref](http://querystream.github.io/querystream/site/apidocs/index.html?org/dellroad/querystream/jpa/Ref.html) objects give you a way to refer to items in the stream pipeline at a later step, by `bind()`'ing the reference at an earlier step.

References also help code clarity, because they provide a way to give meaningful names to important expressions.

See the `getHighPayrollManagers()` example above for how it works. The main thing to remember is that the `bind()` must occur prior to the use of the reference in the pipeline.

## Subqueries

Any stream can be converted into a subquery using `asSubquery()` or `exists()`.

To find all managers for whom there exists a direct report making over $100,000:

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

Note the subquery correlation done via `CriteriaBuilder.equal()`.

To find all employees with salary greater than the average of their manager's direct reports:

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

## Unsupported Operations

Operations that apply to a JPA `TypedQuery` but not a `CriteriaQuery`, such `setMaxResults()`, `setLockMode()`, `setParameter()`, etc., are not supported by the `QueryStream` API.

Instead, invoke `QueryStream.toQuery()`, and then perform these operations directly on the returned `TypedQuery`.

### Installation

QueryStream is available from [Maven Central](http://search.maven.org/#search|ga|1|a%3Aquerystream-jpa):

```xml
    <dependency>
        <groupId>org.dellroad</groupId>
        <artifactId>querystream-jpa</artifactId>
    </dependency>
```

### API Javadocs

Located [here](https://querystream.github.io/querystream/site/apidocs/index.html?org/dellroad/querystream/jpa/QueryStream.Builder.html).
