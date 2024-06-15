package andrehsvictor.inscribe.payload;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Payload<T> {
    private boolean success;
    private String message;
    private T data;
    private Integer page;
    private Integer size;
    private Long totalElements;
    private Integer totalPages;
    private boolean last;
    private String sort;
}
