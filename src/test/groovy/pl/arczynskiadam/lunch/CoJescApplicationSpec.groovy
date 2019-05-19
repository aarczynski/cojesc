package pl.arczynskiadam.lunch

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import spock.lang.Specification

@SpringBootTest(classes = CoJescApplication)
class CoJescApplicationSpec extends Specification {

    @Autowired
    private ApplicationContext applicationContext

    def "spring application context should run"() {
        expect:
        applicationContext
    }
}
