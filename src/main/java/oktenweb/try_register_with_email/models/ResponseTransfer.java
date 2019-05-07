package oktenweb.try_register_with_email.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ResponseTransfer {

    String text;
}
