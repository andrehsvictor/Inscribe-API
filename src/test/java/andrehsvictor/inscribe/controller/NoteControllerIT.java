package andrehsvictor.inscribe.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.not;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import andrehsvictor.inscribe.entity.User;
import andrehsvictor.inscribe.payload.request.NoteRequest;
import andrehsvictor.inscribe.payload.request.SigninRequest;
import andrehsvictor.inscribe.payload.response.NoteResponse;
import andrehsvictor.inscribe.repository.NoteRepository;
import andrehsvictor.inscribe.repository.UserRepository;
import andrehsvictor.inscribe.util.AbstractControllerIT;
import static io.restassured.RestAssured.given;

class NoteControllerIT extends AbstractControllerIT {

    @Autowired
    UserRepository userRepository;

    @Autowired
    NoteRepository noteRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    String accessToken;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        noteRepository.deleteAll();

        User user = new User("André Victor", "andrevictor@inscribe.io", passwordEncoder.encode("12345678"));
        user.activate();
        userRepository.save(user);

        accessToken = given()
                .contentType("application/json")
                .body(new SigninRequest("andrevictor@inscribe.io", "12345678"))
                .when()
                .post("/auth/signin")
                .then()
                .statusCode(200)
                .extract()
                .path("data.accessToken");
    }

    @Test
    void findAll_whenNoNotes_shouldReturnEmptyList() {
        given()
                .auth().oauth2(accessToken)
                .when()
                .get("/api/v1/notes")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("message", equalTo("Notes retrieved successfully"))
                .body("data", hasSize(0))
                .body("page", is(0))
                .body("size", is(20))
                .body("totalElements", is(0))
                .body("totalPages", is(0))
                .body("last", is(true))
                .body("sort", equalTo("UNSORTED"));
    }

    @Test
    void findAll_whenNotesExist_shouldReturnListOfNotes() {
        NoteResponse note = createAndRetrieveNote("First Note", "This is the first note");

        given()
                .auth().oauth2(accessToken)
                .when()
                .get("/api/v1/notes")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("message", equalTo("Notes retrieved successfully"))
                .body("data", hasSize(1))
                .body("data[0].id", equalTo(note.getPublicId()))
                .body("data[0].title", equalTo("First Note"))
                .body("data[0].content", equalTo("This is the first note"))
                .body("data[0].createdAt",
                        matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}.+"))
                .body("data[0].updatedAt",
                        matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}.+"))
                .body("page", is(0))
                .body("size", is(20))
                .body("totalElements", is(1))
                .body("totalPages", is(1))
                .body("last", is(true))
                .body("sort", equalTo("UNSORTED"));
    }

    @Test
    void findAll_whenThereAreMoreThanOnePageOfNotes_shouldReturnPaginatedListOfNotes() {
        for (int i = 0; i < 25; i++) {
            createAndRetrieveNote("Note " + i, "This is note " + i);
        }

        given()
                .auth().oauth2(accessToken)
                .when()
                .get("/api/v1/notes")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("message", equalTo("Notes retrieved successfully"))
                .body("data", hasSize(20))
                .body("page", is(0))
                .body("size", is(20))
                .body("totalElements", is(25))
                .body("totalPages", is(2))
                .body("last", is(false))
                .body("sort", equalTo("UNSORTED"));

        given()
                .auth().oauth2(accessToken)
                .queryParam("page", 1)
                .when()
                .get("/api/v1/notes")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("message", equalTo("Notes retrieved successfully"))
                .body("data", hasSize(5))
                .body("page", is(1))
                .body("size", is(20))
                .body("totalElements", is(25))
                .body("totalPages", is(2))
                .body("last", is(true))
                .body("sort", equalTo("UNSORTED"));
    }

    @Test
    void findById_whenNoteExists_shouldReturnNote() {
        NoteResponse note = createAndRetrieveNote("First Note", "This is the first note");

        given()
                .auth().oauth2(accessToken)
                .when()
                .get("/api/v1/notes/{id}", note.getPublicId())
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("message", equalTo("Note retrieved successfully"))
                .body("data.id", equalTo(note.getPublicId()))
                .body("data.title", equalTo("First Note"))
                .body("data.content", equalTo("This is the first note"))
                .body("data.createdAt",
                        matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}.+"))
                .body("data.updatedAt",
                        matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}.+"));
    }

    @Test
    void create_whenValidNote_shouldReturnCreatedNote() {
        given()
                .auth().oauth2(accessToken)
                .body(new NoteRequest("First Note", "This is the first note"))
                .contentType("application/json")
                .when()
                .post("/api/v1/notes")
                .then()
                .statusCode(201)
                .body("success", is(true))
                .body("message", equalTo("Note created successfully"))
                .body("data.title", equalTo("First Note"))
                .body("data.content", equalTo("This is the first note"))
                .body("data.createdAt",
                        matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}.+"))
                .body("data.updatedAt",
                        matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}.+"));
    }

    @Test
    void update_whenValidNote_shouldReturnUpdatedNote() {
        NoteResponse note = createAndRetrieveNote("First Note", "This is the first note");

        given()
                .auth().oauth2(accessToken)
                .contentType("application/json")
                .body(new NoteRequest("Updated Note", "This is the updated note"))
                .when()
                .put("/api/v1/notes/{id}", note.getPublicId())
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("message", equalTo("Note updated successfully"))
                .body("data.id", equalTo(note.getPublicId()))
                .body("data.title", equalTo("Updated Note"))
                .body("data.content", equalTo("This is the updated note"))
                .body("data.createdAt",
                        matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}.+"))
                .body("data.updatedAt", not(equalTo(note.getUpdatedAt())));
    }

    @Test
    void delete_whenNoteExists_shouldDeleteNote() {
        NoteResponse note = createAndRetrieveNote("First Note", "This is the first note");

        given()
                .auth().oauth2(accessToken)
                .when()
                .delete("/api/v1/notes/{id}", note.getPublicId())
                .then()
                .statusCode(204);

        given()
                .auth().oauth2(accessToken)
                .when()
                .get("/api/v1/notes/{id}", note.getPublicId())
                .then()
                .statusCode(404)
                .body("success", is(false))
                .body("message", equalTo("Note not found"));
    }

    @Test
    void givenThreeNotesExist_whenOneNoteIsDeletedAndOneNoteIsUpdated_thenTwoNotesShouldBeReturned() {
        NoteResponse note1 = createAndRetrieveNote("First Note", "This is the first note");
        NoteResponse note2 = createAndRetrieveNote("Second Note", "This is the second note");
        NoteResponse note3 = createAndRetrieveNote("Third Note", "This is the third note");

        given()
                .auth().oauth2(accessToken)
                .when()
                .get("/api/v1/notes")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("message", equalTo("Notes retrieved successfully"))
                .body("data", hasSize(3))
                .body("data[0].id", equalTo(note1.getPublicId()))
                .body("data[0].title", equalTo("First Note"))
                .body("data[0].content", equalTo("This is the first note"))
                .body("data[1].id", equalTo(note2.getPublicId()))
                .body("data[1].title", equalTo("Second Note"))
                .body("data[1].content", equalTo("This is the second note"))
                .body("data[2].id", equalTo(note3.getPublicId()))
                .body("data[2].title", equalTo("Third Note"))
                .body("data[2].content", equalTo("This is the third note"));

        given()
                .auth().oauth2(accessToken)
                .when()
                .delete("/api/v1/notes/{id}", note1.getPublicId())
                .then()
                .statusCode(204);

        given()
                .auth().oauth2(accessToken)
                .contentType("application/json")
                .body(new NoteRequest("Updated Third Note", "This is the updated third note"))
                .when()
                .put("/api/v1/notes/{id}", note3.getPublicId())
                .then()
                .statusCode(200);

        given()
                .auth().oauth2(accessToken)
                .when()
                .get("/api/v1/notes")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("message", equalTo("Notes retrieved successfully"))
                .body("data", hasSize(2))
                .body("data[0].id", equalTo(note2.getPublicId()))
                .body("data[0].title", equalTo("Second Note"))
                .body("data[0].content", equalTo("This is the second note"))
                .body("data[1].id", equalTo(note3.getPublicId()))
                .body("data[1].title", equalTo("Updated Third Note"))
                .body("data[1].content", equalTo("This is the updated third note"));
    }

    private NoteResponse createAndRetrieveNote(String title, String content) {
        NoteResponse note = given()
                .auth().oauth2(accessToken)
                .contentType("application/json")
                .body(new NoteRequest(title, content))
                .when()
                .post("/api/v1/notes")
                .then()
                .statusCode(201)
                .extract()
                .body()
                .jsonPath()
                .getObject("data", NoteResponse.class);
        return note;
    }

}
