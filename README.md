### QueryStream

QueryStream allows you to perform JPA queries using a `Stream`-like API.

Just like a Java 8 `Stream`, a `QueryStream` is built up in a pipeline, using methods like `map()`, `flatMap()`, `filter()`, etc.

Each step in a `QueryStream` pipeline modifies the construction of an internal [JPA Criteria query](https://docs.oracle.com/javaee/7/api/?javax/persistence/criteria/package-summary.html).

When you're ready to execute the pipeline:

  1. Invoke `QueryStream.toCriteriaQuery()` to extract the [CriteriaQuery](https://docs.oracle.com/javaee/7/api/?javax/persistence/criteria/CriteriaQuery.html); or
  1. Invoke `QueryStream.toQuery()` to do #1 and also create a [TypedQuery](https://docs.oracle.com/javaee/7/api/?javax/persistence/TypedQuery.html); or
  1. Invoke `QueryStream.getResultList()` to do #1 and #2 then execute the query

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

With QueryStream, your code becomes more succint and visually intuitive:

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
          .filter(v -> qb.greaterThan(v, 50000))
          .bind(avgSalary)
          .groupBy(manager)
          .orderBy(avgSalary, false)
          .getResultList();
    }

    @PersistenceContext
    private EntityManager entityManager;
    private QueryStream.Builder qb;

    @PostConstruct
    private void setupBuilders() {
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

Some queries are known to return a single value. The [SearchValue](http://querystream.github.io/querystream/site/apidocs/index.html?org/dellroad/querystream/jpa/SearchValue.html) and its subinterfaces represent streams for which it is known that at most one result will be found. These interfaces have a `value()` method, which executes the query and returns the single value:

```java
    public double getAverageSalary(Employee manager) {
        return qb.stream(Employee.class)
          .filter(e -> qb.equal(e, manager))
          .flatMap(Employee_.directReports)
          .mapToDouble(Employee_.salary)
          .average()
          .value();
    }
```

Other similar methods are `min()`, `max()`, `sum()`, and `findFirst()`.

## References

[Ref](http://querystream.github.io/querystream/site/apidocs/index.html?org/dellroad/querystream/jpa/Ref.html) objects give you a way to refer to items in the stream pipeline at a later step, by `bind()`'ing the reference at an earlier step.

References also help code clarity, because they provide a way to give meaningful names to important expressions.

See the `getHighPayrollManagers()` example above for how it works. The main thing to remember is that the `bind()` must occur prior to the use of the reference in the pipeline.

## Subqueries

QueryStream makes using subqueries easier. A stream can be used as a subquery via `asSubquery()` or `exists()`.

To find all managers for whom there exists a direct report making over $100,000:

```java
    public List<Employee> findManagersWithSixFigureDirectReport() {
        return qb.stream(Employee.class)
          .filter(manager -> qb.stream(Employee.class)
                .filter(report -> qb.and(
                    qb.equal(report.get(Employee_.manager), manager),
                    qb.greaterThan(report.get(Employee_.salary), 100000.0)))
                .exists());
    }
```

Note the subquery correlation was done "manually" using `CriteriaBuilder.equal()`; you can clean this up a bit with an explicit correlation using `substream()`:

```java
    public List<Employee> findManagersWithSixFigureDirectReport() {
        return qb.stream(Employee.class)
          .filter(manager -> qb.substream(manager)
            .flatMap(Employee_.directReports)
            .filter(report -> qb.greaterThan(report.get(Employee_.salary), 100000.0))
            .exists());
    }
```

To find all employees with salary greater than the average of their manager's direct reports:

```java
    public List<Employee> findEmployeesWithAboveAverageSalaries() {
        return qb.stream(Employee.class)
          .filter(employee -> qb.greaterThan(
              employee.get(Employee_.salary),
              qb.stream(Employee.class)
                .filter(coworker ->
                  qb.equal(
                    coworker.get(Employee_.manager),
                    employee.get(Employee_.manager)))
                .mapToDouble(Employee_.salary)
                .average()
                .asSubquery()))
          .getResultList();
    }
```

Hmmm, that's a lot of nesting. You could make the code clearer by building the subquery separately, and using a reference for the correlation:

```java
    public DoubleValue getAvgCoworkerSalary(RootRef<Employee> employee) {
        return qb.stream(Employee.class)
          .filter(coworker -> qb.equal(coworker.get(Employee_.manager), employee.get().get(Employee_.manager)))
          .mapToDouble(Employee_.salary)
          .average();
    }

    public List<Employee> findEmployeesWithAboveAverageSalaries() {
        final RootRef<Employee> employee = new RootRef<>();
        return qb.stream(Employee.class)
          .bind(employee)
          .filter(employee ->
            qb.greaterThan(employee.get(Employee_.salary), getAvgCoworkerSalary(employee).asSubquery()))
          .getResultList();
    }
```
That's really just regular Java refactoring.

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
          .mapToSelection(Object[].class, e -> qb.array(manager.get(), avgSalary.get()))
          .orderBy(avgSalary, false);
          .having(avgSalary -> qb.gt(avgSalary, 50000.0))
          .getResultList();
    }
```

## Offset and Limit

Use `skip()` and `limit()` to set the row offset and the maximum number of results.

## `CriteriaQuery` vs `TypedQuery` Operations

Normally with JPA you first configure a `CriteriaQuery`, then use it to create a `TypedQuery`, and then do additional configuration on the `TypedQuery`. You must apply configuration to the appropriate object for that configuration.

For example, `where()` is a `CriteriaQuery` method, but `setLockMode()` is a `TypedQuery` method.

`QueryStream` pipelines allow you to configure both the `CriteriaQuery` and the `TypedQuery`, but you should be aware of these caveats:

  * Any `TypedQuery` configuration must come at the end of the pipeline (after any subqueries or joins)
  * If you invoke `QueryStream.toCriteriaQuery()`, the returned object does not retain any `TypedQuery` configuration (instead, invoke `QueryStream.toQuery()`).

The `QueryStream` methods that configure the `TypedQuery` are:

 * `QueryStream.skip()`
 * `QueryStream.limit()`
 * `QueryStream.withFlushMode()`
 * `QueryStream.withLockMode()`
 * `QueryStream.withFetchGraph()`
 * `QueryStream.withLoadGraph()`
 * `QueryStream.withHint()` and `QueryStream.withHints()`

## Unsupported Operations

In some cases, limitations in the JPA Criteria API impose certain restrictions on what you can do.

For example, `skip()` and `limit()` must be at the end of your pipeline, because the JPA Criteria API doesn't allow setting row offset or the maximum results on a subquery or prior to a join.

For another example, you can't sort in a subquery.

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
