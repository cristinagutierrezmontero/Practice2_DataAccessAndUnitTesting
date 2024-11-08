package com.quarkuscourse.resource;

import com.quarkuscourse.entity.Department;
import com.quarkuscourse.repository.DepartmentRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.smallrye.mutiny.Uni;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.never;

@QuarkusTest
@TestHTTPEndpoint(DepartmentResource.class)
public class DepartmentResourceTest {

    @InjectMock
    DepartmentRepository departmentRepository;

    ObjectId existId = new ObjectId();
    ObjectId notExistId = new ObjectId();

    @BeforeEach
    public void setup() {

        Department department1 = new Department("Tech");
        Department department2 = new Department("RRHH");

        Mockito.when(departmentRepository.findById(existId))
                .thenReturn(Uni.createFrom().item(department1));

        Mockito.when(departmentRepository.findById(notExistId))
                .thenReturn(Uni.createFrom().nullItem());

        Mockito.when(departmentRepository.persist(Mockito.any(Department.class)))
                .thenReturn(Uni.createFrom().item(department2));

        Mockito.when(departmentRepository.delete(department1))
                .thenReturn(Uni.createFrom().nullItem());
    }

    @Test
    public void testGetDepartmentById() {
        given()
                .pathParam("id", existId.toHexString())
                .when().get("/{id}")
                .then()
                .statusCode(200)
                .body("name", is("Tech"));

        Mockito.verify(departmentRepository).findById(existId);
    }

    @Test
    public void testGetNonExistentDepartment() {
        given()
                .pathParam("id", notExistId.toHexString())
                .when().get("/{id}")
                .then()
                .statusCode(404);

        Mockito.verify(departmentRepository).findById(notExistId);
    }

    @Test
    public void testCreateDepartment() {
        Department department3 = new Department( "Tech");

        given()
                .contentType(ContentType.JSON)
                .body(department3)
                .when().post()
                .then()
                .statusCode(200);

        Mockito.verify(departmentRepository).persist(department3);
    }

    @Test
    public void testUpdateDepartment() {
        Department department1 = new Department("Tech");
        Mockito.when(departmentRepository.update(Mockito.any(Department.class)))
                .thenReturn(Uni.createFrom().item(department1));

        given()
                .pathParam("id", existId.toHexString())
                .contentType(ContentType.JSON)
                .body(department1)
                .when().put("/{id}")
                .then()
                .statusCode(200)
                .body("name", is("Tech"));

        Mockito.verify(departmentRepository).findById(existId);
        Mockito.verify(departmentRepository).update(department1);
    }

    @Test
    public void testUpdateDepartmentByIdNotFound() {
        given()
                .pathParam("id", notExistId.toHexString())
                .when().put("/{id}")
                .then()
                .statusCode(404);

        Mockito.verify(departmentRepository).findById(notExistId);
        Mockito.verify(departmentRepository, never()).update(Mockito.any(Department.class));
    }

    @Test
    public void testDeleteDepartmentById() {
        given()
                .pathParam("id", existId.toHexString())
                .when().delete("/{id}")
                .then()
                .statusCode(204)
                .body(is(""));

        Mockito.verify(departmentRepository).findById(existId);
        Mockito.verify(departmentRepository).delete(Mockito.any(Department.class));
    }

    @Test
    public void testDeleteDepartmentByIdNotFound() {
        given()
                .pathParam("id", notExistId.toHexString())
                .when().delete("/{id}")
                .then()
                .statusCode(404);

        Mockito.verify(departmentRepository).findById(notExistId);
        Mockito.verify(departmentRepository, never()).deleteById(notExistId);
    }

}
