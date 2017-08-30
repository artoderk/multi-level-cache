import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by xiong.jie on 2017-07-17.
 */
@Setter
@Getter
@ToString
public class TestBean<T> {

    String id;

    String name;

    T bean;
}
