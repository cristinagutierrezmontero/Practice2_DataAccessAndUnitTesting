package com.quarkuscourse.repository;

import com.quarkuscourse.entity.Department;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;

@ApplicationScoped
public class DepartmentRepository implements ReactivePanacheMongoRepositoryBase<Department, ObjectId> {
}


