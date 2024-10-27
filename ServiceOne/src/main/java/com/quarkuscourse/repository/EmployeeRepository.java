package com.quarkuscourse.repository;

import com.quarkuscourse.entity.Employee;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;

@ApplicationScoped
public class EmployeeRepository implements ReactivePanacheMongoRepositoryBase<Employee, ObjectId> {
}