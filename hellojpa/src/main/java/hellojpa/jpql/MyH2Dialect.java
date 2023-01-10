package hellojpa.jpql;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

// DB 방언을 상속
public class MyH2Dialect extends H2Dialect {
    
    // 사용자 정의 함수 추가
    // 해당 함수를 사용하려면 DB 설정 파일에 이 방언을 사용하도록 변경해야 함
    // <property name="hibernate.dialect" value="hellojpa.jpql.MyH2Dialect"/>
    public MyH2Dialect() {
        registerFunction("group_concat", new StandardSQLFunction("group_concat", StandardBasicTypes.STRING));
    }
}
