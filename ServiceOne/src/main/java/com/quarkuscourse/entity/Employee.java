package com.quarkuscourse.entity;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntityBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@MongoEntity(database = "my-company", collection = "employees")
    public class Employee extends ReactivePanacheMongoEntityBase {
        @BsonId
        private ObjectId id;
        @NonNull
        private String name;
        @NonNull
        private Department department;

    }

