
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.jpa;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.dellroad.querystream.jpa.test.Employee;
import org.dellroad.querystream.jpa.test.Employee_;
import org.dellroad.stuff.test.TestSupport;
import org.hibernate.Session;
import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class QueryTest extends TestSupport {

    private ClassPathXmlApplicationContext context;

    @Value("classpath:META-INF/querystream-test/h2.sql")
    private Resource initialDDL;

    @PersistenceContext
    private EntityManager entityManager;
    private QueryStream.Builder qb;

// Tests

    @Test(dataProvider = "testQueries")
    @Transactional
    public void testQueries(TestCase testCase) throws Exception {

        // Get generated SQL query
        String actual = this.toSQL(testCase.getStream().toQuery());

        // Normalize
        actual = actual.trim().replaceAll("\\s+", " ");
        final String expected = testCase.getSQL().trim().replaceAll("\\s+", " ");
        this.log.info("SQL: " + new BasicFormatterImpl().format(actual));

        // Compare generated SQL to expected
        Assert.assertEquals(actual, expected);

        // Execute query
        final QueryStream<?, ?, ?, ?, ?> stream = testCase.getStream();
        if (stream instanceof SearchStream)
            ((SearchStream<?, ?>)stream).getResultList().size();
        else if (stream instanceof UpdateStream)
            ((UpdateStream<?>)stream).update();
        else if (stream instanceof DeleteStream)
            ((DeleteStream<?>)stream).delete();
        else
            throw new RuntimeException("what is this? " + stream);
    }

    @DataProvider(name = "testQueries")
    public TestCase[][] genTestQueries() throws Exception {
        final TestCase[] testCases = new TestCase[] {

        // Find all employees
        new TestCase(() -> this.qb.stream(Employee.class),
          "select generatedAlias0 from Employee as generatedAlias0"),

        // Find all employees with salary > 50k
        new TestCase(() -> this.qb.stream(Employee.class)
          .filter(e -> this.qb.gt(e.get(Employee_.salary), 50000.0f)),
          "select generatedAlias0 from Employee as generatedAlias0 where generatedAlias0.salary>50000.0F"),

        // Find average salary of all employess
        new TestCase(() -> this.qb.stream(Employee.class)
          .mapToDouble(Employee_.salary)
          .average(),
          "select avg(generatedAlias0.salary) from Employee as generatedAlias0"),

        // Find all employess with salary greater than the average for their department
        new TestCase(() -> this.qb.stream(Employee.class)
          .filter(e ->
            this.qb.gt(e.get(Employee_.salary),
            this.qb.stream(Employee.class)
              .filter(e2 -> this.qb.equal(e2.get(Employee_.department), e.get(Employee_.department)))
              .mapToDouble(Employee_.salary)
              .average()
              .asSubquery())),
          "select generatedAlias0 from Employee as generatedAlias0"
            + " where generatedAlias0.salary>( "
            + "select avg(generatedAlias1.salary) from Employee as generatedAlias1"
            + " where generatedAlias1.department=generatedAlias0.department )"),

        // Find all employees whose manager has a direct report named "fred" using subquery
        new TestCase(() -> {
            return this.qb.stream(Employee.class)
              .filter(e -> this.qb.stream(Employee.class)
                                    .filter(manager -> this.qb.equal(manager, e.get(Employee_.manager)))
                                    .join(Employee_.directReports)
                                    .filter(report -> this.qb.equal(report.get(Employee_.name), "fred"))
                                    .exists());
          },
          "select generatedAlias0 from Employee as generatedAlias0"
            + " where exists ( "
            + "select generatedAlias1 from Employee as generatedAlias2"
            + " inner join generatedAlias2.directReports as generatedAlias1"
            + " where ( generatedAlias2=generatedAlias0.manager ) and ( generatedAlias1.name=:param0 ) )"),

        // Find all employees whose manager has a direct report named "fred" using substream()
        new TestCase(() -> {
            return this.qb.stream(Employee.class)
              .filter(e -> this.qb.substream(e)
                                    .map(Employee_.manager)
                                    .join(Employee_.directReports)
                                    .filter(report -> this.qb.equal(report.get(Employee_.name), "fred"))
                                    .exists());
          },
          "select generatedAlias0 from Employee as generatedAlias0"
           + " where exists ( "
           + "select generatedAlias1 from generatedAlias0.manager as generatedAlias2"
           + " inner join generatedAlias2.directReports as generatedAlias1"
           + " where generatedAlias1.name=:param0 )"),

        // Find all managers paired with the average salary of their direct reports
        // where the average salary is at least 50k, sorted by average salary descending
        new TestCase(() -> {
            final RootRef<Employee> managerRef = new RootRef<>();
            final ExprRef<Double> avgSalaryRef = new ExprRef<>();
            return this.qb.stream(Employee.class)
              .bind(managerRef)
              .flatMap(Employee_.directReports)
              .mapToDouble(Employee_.salary)
              .average()
              .bind(avgSalaryRef)
              .groupBy(managerRef)
              .having(avgSalary -> this.qb.gt(avgSalary, 50000))
              .mapToSelection(Object[].class, e -> this.qb.array(managerRef.get(), avgSalaryRef.get()))
              .orderBy(avgSalaryRef, false);
          },
          "select generatedAlias0, avg(generatedAlias1.salary) from Employee as generatedAlias0"
            + " inner join generatedAlias0.directReports as generatedAlias1"
            + " group by generatedAlias0 having avg(generatedAlias1.salary)>50000.0D"
            + " order by avg(generatedAlias1.salary) desc"),

        // Count employees
        new TestCase(() -> this.qb.stream(Employee.class)
            .count(),
          "select count(generatedAlias0) from Employee as generatedAlias0"),

        };

        // Done
        return Arrays.asList(testCases).stream()
          .map(tc -> new TestCase[] { tc })
          .collect(Collectors.toList())
          .toArray(new TestCase[testCases.length][]);
    }

    @Test
    public void testBadSubstream() throws Exception {
        final Root<Employee> root = this.qb.createQuery().from(Employee.class);
        try {
            this.qb.substream(root).toQuery();
            assert false : "expected exception";
        } catch (IllegalArgumentException e) {
            this.log.debug("got expected " + e);
        }
    }

    @Test
    @Transactional
    public void testSkipLimit() throws Exception {

        final Employee e1 = new Employee();
        e1.setName("aaa");
        e1.setSalary(20000.0f);
        this.entityManager.persist(e1);

        final Employee e2 = new Employee();
        e2.setName("bbb");
        e2.setSalary(15000.0f);
        this.entityManager.persist(e2);

        final Employee e3 = new Employee();
        e3.setName("ccc");
        e3.setSalary(25000.0f);
        this.entityManager.persist(e3);

        Assert.assertEquals(this.qb.stream(Employee.class)
          .orderBy(Employee_.name, true)
          .limit(1)
          .findFirst()
          .value(), e1);

        Assert.assertEquals(this.qb.stream(Employee.class)
          .orderBy(Employee_.name, true)
          .limit(100)
          .findFirst()
          .value(), e1);

        Assert.assertEquals(this.qb.stream(Employee.class)
          .orderBy(Employee_.name, true)
          .skip(1)
          .limit(1)
          .findFirst()
          .value(), e2);

        Assert.assertEquals(this.qb.stream(Employee.class)
          .orderBy(Employee_.name, true)
          .skip(2)
          .findFirst()
          .value(), e3);

        Assert.assertEquals(this.qb.stream(Employee.class)
          .orderBy(Employee_.salary, false)
          .skip(2)
          .findAny()
          .value(), e2);

// can't get this to work for some reason?
//        Assert.assertEquals(this.qb.stream(Employee.class)
//          .limit(0)
//          .findAny()
//          .orElse(null), null);
    }

// Illegal operations after skip() or limit()

    @Test(dataProvider = "badSkipLimitUsageFunctions")
    @Transactional
    public void testBadSkipLimitUsage(Function<RootStream<Employee>, ? extends SearchStream<?, ?>> function) throws Exception {
        RootStream<Employee> stream = this.qb.stream(Employee.class);
        stream = this.random.nextBoolean() ? stream.skip(1) : stream.limit(2);
        try {
            function.apply(stream).getResultList();
            assert false;
        } catch (UnsupportedOperationException e) {
            this.log.debug("got expected " + e);
        }
    }

    // Stuff that you can't do after limit() or skip()
    @DataProvider(name = "badSkipLimitUsageFunctions")
    public Object[][] genBadSkipLimitUsageFunctions() throws Exception {
        final List<Function<RootStream<Employee>, ? extends SearchStream<?, ?>>> funcs = new ArrayList<>();
        funcs.add(s -> s.orderBy(Employee_.salary, false));
        funcs.add(s -> s.filter(e -> this.qb.equal(e.get(Employee_.name), "foobar")));
        funcs.add(s -> s.map(Employee_.name));
        funcs.add(s -> s.countDistinct());
        funcs.add(s -> s.count());
        return funcs.stream()
          .map(func -> new Object[] { func })
          .toArray(Object[][]::new);
    }

// Illegal Subquery operations

    @Test(dataProvider = "badSubqueryFunctions")
    @Transactional
    public void testBadSubqueryUsage(Function<RootStream<Employee>, ? extends ExprStream<?, ?>> function) throws Exception {
        try {
            this.qb.stream(Employee.class)
              .filter(e -> function.apply(this.qb.substream(e)).exists())
              .toQuery();
            assert false;
        } catch (UnsupportedOperationException e) {
            this.log.debug("got expected " + e);
        }
    }

    // Stuff that you can't do within a subquery
    @DataProvider(name = "badSubqueryFunctions")
    public Object[][] genBadSubqueryFunctions() throws Exception {
        final List<Function<RootStream<Employee>, ? extends ExprStream<?, ?>>> funcs = new ArrayList<>();
        funcs.add(s -> s.limit(10));
        funcs.add(s -> s.skip(10));
        return funcs.stream()
          .map(func -> new Object[] { func })
          .toArray(Object[][]::new);
    }

// Check QueryInfo from subquery merging with outer query

    @Test
    public void testSubqueryNonconflictingParam() throws Exception {
        final ParameterExpression<String> nameParam = this.qb.parameter(String.class, "name");
        final ParameterExpression<Float> salaryParam = this.qb.parameter(Float.class, "salary");
        this.qb.stream(Employee.class)
          .filter(e -> this.qb.substream(e)
                        .flatMap(Employee_.directReports)
                        .filter(e2 -> this.qb.equal(e2.get(Employee_.salary), salaryParam))
                        .withParam(salaryParam, 123.4f)
                        .exists())
          .filter(e -> this.qb.equal(e.get(Employee_.name), nameParam))
          .withParam(nameParam, "Jeff")
          .toQuery();
    }

    @Test
    public void testSubqueryConflictingParam() throws Exception {
        try {
            final ParameterExpression<String> nameParam = this.qb.parameter(String.class, "name");
            this.qb.stream(Employee.class)
              .filter(e -> qb.substream(e)
                            .withParam(nameParam, "Fred")                       // set parameter to "Fred" in inner subquery
                            .exists())
              .filter(e -> qb.equal(e.get(Employee_.name), nameParam))
              .withParam(nameParam, "Jeff")                                     // set parameter to "Jeff" in outer query
              .toQuery();
            assert false : "expected exception";
        } catch (IllegalArgumentException e) {
            this.log.debug("got expected " + e);
        }
    }

    @Test
    public void testSubquerySameParam() throws Exception {
        final ParameterExpression<String> nameParam = this.qb.parameter(String.class, "name");
        this.qb.stream(Employee.class)
          .filter(e -> qb.substream(e)
                        .flatMap(Employee_.directReports)
                        .filter(e2 -> this.qb.equal(e2.get(Employee_.name), nameParam))
                        .withParam(nameParam, "Fred")                       // set parameter to "Fred" in inner subquery
                        .exists())
          .filter(e -> qb.equal(e.get(Employee_.name), nameParam))
          .withParam(nameParam, "Fred")                                     // set parameter to "Fred" in outer query
          .toQuery();
    }

    @Test
    public void testSubquerySameParam2() throws Exception {
        final ParameterExpression<String> nameParam1 = this.qb.parameter(String.class, "name");
        final ParameterExpression<String> nameParam2 = this.qb.parameter(String.class, "name");
        this.qb.stream(Employee.class)
          .filter(e -> qb.substream(e)
                        .flatMap(Employee_.directReports)
                        .filter(e2 -> this.qb.equal(e2.get(Employee_.name), nameParam1))
                        .withParam(nameParam1, "Fred")                      // set parameter to "Fred" in inner subquery
                        .exists())
          .filter(e -> qb.equal(e.get(Employee_.name), nameParam2))
          .withParam(nameParam2, "Fred")                                    // set parameter to "Fred" in outer query
          .toQuery();
    }

// TestCase

    public static class TestCase {

        private final Supplier<? extends QueryStream<?, ?, ?, ?, ?>> query;
        private final String sql;

        public TestCase(Supplier<? extends QueryStream<?, ?, ?, ?, ?>> query, String sql) {
            this.query = query;
            this.sql = sql;
        }

        public QueryStream<?, ?, ?, ?, ?> getStream() {
            return this.query.get();
        }

        public String getSQL() {
            return this.sql;
        }
    }

// SQL decoder

    public String toSQL(Query query) {
        return new BasicFormatterImpl().format(query.unwrap(org.hibernate.query.Query.class).getQueryString());
    }

// Test Lifecycle

    @BeforeClass
    public void setup() throws Exception {
        this.log.info("SETUP: opening application context");
        this.context = new ClassPathXmlApplicationContext("queryTest.xml", this.getClass());
        this.log.info("SETUP: autowiring " + this.getClass().getName());
        this.context.getAutowireCapableBeanFactory().autowireBean(this);
        this.log.info("SETUP: initializing database");
        this.initializeDB();
        this.qb = QueryStream.newBuilder(this.entityManager);
    }

    @AfterClass
    public void shutdown() throws Exception {
        this.log.info("SHUTDOWN: closing application context");
        this.context.destroy();
        this.context = null;
    }

    @Transactional
    public void initializeDB() throws SQLException {
        this.entityManager.unwrap(Session.class).doWork(this::initializeDB);
    }

    public void initializeDB(Connection connection) throws SQLException {
        this.log.info("applying intial DDL: {}", this.initialDDL);
        try (final InputStream input = this.initialDDL.getInputStream()) {
            for (String command : StreamUtils.copyToString(input, StandardCharsets.UTF_8).split(";")) {
                if ((command = command.trim()).isEmpty())
                    continue;
                this.log.info("applying intial DDL command: {}", command);
                try (final Statement statement = connection.createStatement()) {
                    statement.execute(command);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("unexpected exception", e);
        }
    }
}
