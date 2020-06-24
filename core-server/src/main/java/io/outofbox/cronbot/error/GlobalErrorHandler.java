package io.outofbox.cronbot.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Global error handler controller
 * @author ahelmy
 */
@ControllerAdvice
public class GlobalErrorHandler  extends ResponseEntityExceptionHandler {



    /**
     * Conflit exception handling
     * @param req
     * @param ex
     * @return
     */
    @ExceptionHandler(ConflictExcpetion.class)
    public ApiError handleConflict(HttpServletRequest req, Exception ex){
        return new ApiError(HttpStatus.CONFLICT,ex.getMessage(),ex);
    }
    /**
     * Not found exception handling
     * @param req
     * @param ex
     * @return
     */
    @ExceptionHandler(NotFoundException.class)
    public ApiError handleNotFound(HttpServletRequest req, Exception ex){
        return new ApiError(HttpStatus.NOT_FOUND,ex.getMessage(),ex);
    }

    /**
     * A single place to customize the response body of all exception types.
     * <p>The default implementation sets the {@link WebUtils#ERROR_EXCEPTION_ATTRIBUTE}
     * request attribute and creates a {@link ResponseEntity} from the given
     * body, headers, and status.
     * @param ex the exception
     * @param body the body for the response
     * @param headers the headers for the response
     * @param status the response status
     * @param request the current request
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }
        ApiError apiError = new ApiError(status, ex.getMessage(), ex);
        return new ResponseEntity<>(apiError, headers, status);
    }
}
