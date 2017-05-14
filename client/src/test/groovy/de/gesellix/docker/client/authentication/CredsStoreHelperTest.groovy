package de.gesellix.docker.client.authentication

import de.gesellix.docker.client.config.DockerEnv
import spock.lang.Requires
import spock.lang.Specification

class CredsStoreHelperTest extends Specification {

    CredsStoreHelper helper

    def setup() {
        helper = new CredsStoreHelper()
    }

    @Requires({ System.properties['user.name'] == 'gesellix' })
    def "can retrieve auth from osxkeychain"() {
        when:
        def result = helper.getAuthentication("osxkeychain")
        then:
        result == [
                auth: [
                        ServerURL: new DockerEnv().indexUrl_v1,
                        Username : "gesellix",
                        Secret   : "-yet-another-password-"
                ]
        ]
    }

    def "handles errors more or less gracefully"() {
        when:
        def result = helper.getAuthentication("osxkeychain", "foo")
        then:
        result.auth == null
        and:
        result.error =~ ".*credentials not found in native keychain.*"
    }
}
