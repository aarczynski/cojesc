package pl.arczynskiadam.cojesc.util

import spock.lang.Specification

class UrlUtilSpec extends Specification {

    def "should sneakily throw MalformedURLException"() {
        when:
        UrlUtil.toUrl("malformed-url")

        then:
        def e = thrown(MalformedURLException)
        e.message == 'no protocol: malformed-url'
    }
}
