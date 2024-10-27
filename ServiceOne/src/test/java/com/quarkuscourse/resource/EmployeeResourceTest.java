package com.quarkuscourse.resource;

import com.quarkuscourse.entity.Department;
import com.quarkuscourse.entity.Employee;
import com.quarkuscourse.repository.EmployeeRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.never;

@QuarkusTest
@TestHTTPEndpoint(EmployeeResource.class)
public class EmployeeResourceTest {
    @InjectMock
    EmployeeRepository employeeRepository;

    ObjectId existId = new ObjectId();
    ObjectId notExistId = new ObjectId();

    @BeforeEach
    public void setup() {
        // Clean BD before each test
        //employeeRepository.deleteAll();

        // Insertar algunos datos de prueba
        Department department = new Department("Tech");
        Employee emp1 = new Employee("Cris", department);
        Employee emp2 = new Employee( "Alfonso", department);
        Employee emp3 = new Employee( "Ricardo", department);

        Mockito.when(employeeRepository.streamAll())
                .thenReturn(Multi.createFrom().items(emp1, emp2));



        Mockito.when(employeeRepository.findById(existId))
                .thenReturn(Uni.createFrom().item(emp1));

        Mockito.when(employeeRepository.findById(notExistId))
                .thenReturn(Uni.createFrom().nullItem());

        Mockito.when(employeeRepository.persist(Mockito.any(Employee.class)))
                .thenReturn(Uni.createFrom().item(emp3));


        Mockito.when(employeeRepository.delete(emp1))
                .thenReturn(Uni.createFrom().nullItem());
    }

    @Test
    public void testGetAllEmployees() {

        given()
                .when().get()
                .then()
                .statusCode(200)
                .body("$", hasSize(2))
                .body("[0].name", is("Cris"))
                .body("[0].department.name", is("Tech"))
                .body("[1].name", is("Alfonso"))
                .body("[1].department.name", is("Tech"));


        Mockito.verify(employeeRepository).streamAll();
    }

    @Test
    public void testGetEmployeeById() {
        given()
                .pathParam("id", existId.toHexString())
                .when().get("/{id}")
                .then()
                .statusCode(200)
                .body("name", is("Cris"))
                .body("department.name", is("Tech"));

        Mockito.verify(employeeRepository).findById(existId);
    }

    @Test
    public void testGetNonExistentEmployee() {
        given()
                .pathParam("id", notExistId.toHexString())
                .when().get("/{id}")
                .then()
                .statusCode(404);

        Mockito.verify(employeeRepository).findById(notExistId);
    }


    @Test
    public void testCreateEmployee() {
        Department department = new Department( "Tech");
        Employee emp3 = new Employee("Ricardo", department);

        given()
                .contentType(ContentType.JSON)
                .body(emp3)
                .when().post()
                .then()
                .statusCode(200);

        Mockito.verify(employeeRepository).persist(emp3);
    }

    @Test
    public void testUpdateEmployee() {
        Department department = new Department("Tech");
        Employee emp1 = new Employee( "Ricardo", department);
        Mockito.when(employeeRepository.update(Mockito.any(Employee.class)))
                .thenReturn(Uni.createFrom().item(emp1));

        given()
                .pathParam("id", existId.toHexString())
                .contentType(ContentType.JSON)
                .body(emp1)
                .when().put("/{id}")
                .then()
                .statusCode(200)
                .body("name", is("Ricardo"));


        Mockito.verify(employeeRepository).findById(existId);
        Mockito.verify(employeeRepository).update(emp1);
    }

    @Test
    public void testUpdateEmployeeByIdNotFound() {
        given()
                .pathParam("id", notExistId.toHexString())
                .when().put("/{id}")
                .then()
                .statusCode(404);

        Mockito.verify(employeeRepository).findById(notExistId);
        Mockito.verify(employeeRepository, never()).update(Mockito.any(Employee.class));
    }

    @Test
    public void testDeleteEmployeeById() {
        given()
                .pathParam("id", existId.toHexString())
                .when().delete("/{id}")
                .then()
                .statusCode(204)
                .body(is(""));

        Mockito.verify(employeeRepository).findById(existId);
        Mockito.verify(employeeRepository).delete(Mockito.any(Employee.class));
    }

    @Test
    public void testDeleteEmployeeByIdNotFound() {
        given()
                .pathParam("id", notExistId.toHexString())
                .when().delete("/{id}")
                .then()
                .statusCode(404);

        Mockito.verify(employeeRepository).findById(notExistId);
        Mockito.verify(employeeRepository, never()).deleteById(notExistId);
    }

}

