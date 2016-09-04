package iexls

import spock.lang.Specification
/**
 * Created by icarokume on 04/09/16.
 */
class MainSpec extends Specification {

    def "test"() {
        given:
        def main = new Main()

        when:
        def result = main.ok()

        then:
        result
    }

}
