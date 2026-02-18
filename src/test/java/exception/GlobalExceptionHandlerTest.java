package exception;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.id.recipes.recipes_api.exception.ErrorDetails;
import com.id.recipes.recipes_api.exception.GlobalExceptionHandler;
import com.id.recipes.recipes_api.exception.RecipeNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @Mock
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        exceptionHandler = new GlobalExceptionHandler();
        when(webRequest.getDescription(false)).thenReturn("uri=/test");
    }

    @Test
    void testResourceNotFoundException() {
        RecipeNotFoundException ex = new RecipeNotFoundException("Recipe not found");

        ResponseEntity<ErrorDetails> response =
                exceptionHandler.resourceNotFoundException(ex, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Recipe not found", response.getBody().getMessage());
        assertEquals("uri=/test", response.getBody().getDetails());
    }

    @Test
    void testConstraintViolationException() {
        ConstraintViolationException ex = new ConstraintViolationException("Invalid field", null);

        ResponseEntity<ErrorDetails> response =
                exceptionHandler.handleConstraintViolationException(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid field", response.getBody().getMessage());
    }

    @Test
    void testHibernateConstraintViolationException() {
        org.hibernate.exception.ConstraintViolationException ex =
                new org.hibernate.exception.ConstraintViolationException("DB constraint", null, null);

        ResponseEntity<ErrorDetails> response =
                exceptionHandler.handleConstraintViolationExceptionHibernate(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("DB constraint", response.getBody().getMessage());
    }

    @Test
    void testMethodArgumentTypeMismatchException() {
        MethodArgumentTypeMismatchException ex =
                new MethodArgumentTypeMismatchException("value", String.class, "param", null, null);

        ResponseEntity<ErrorDetails> response =
                exceptionHandler.handleMethodArgumentTypeMismatchException(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
    }

    @Test
    void testGlobalExceptionHandler() {
        Exception ex = new Exception("Something went wrong");

        ResponseEntity<ErrorDetails> response =
                exceptionHandler.globalExcpetionHandler(ex, webRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Something went wrong", response.getBody().getMessage());
    }
}