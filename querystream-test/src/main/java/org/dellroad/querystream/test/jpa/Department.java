
/*
 * Copyright (C) 2018 Archie L. Cobbs. All rights reserved.
 */

package org.dellroad.querystream.test.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Department extends AbstractPersistent {

    private String name;
    private Set<Employee> employees = new HashSet<>();

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "department")
    public Set<Employee> getEmployees() {
        return this.employees;
    }
    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }
}
