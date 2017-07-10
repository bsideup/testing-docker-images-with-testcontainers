@Grab('org.testcontainers:testcontainers:1.4.1')
@Grab('org.slf4j:slf4j-simple:1.7.25')
import org.testcontainers.containers.*
import org.testcontainers.images.builder.*
import org.junit.*

@Grab('io.rest-assured:rest-assured:3.0.3')
@GrabExclude("org.codehaus.groovy:groovy-xml")
import io.restassured.*
import static io.restassured.RestAssured.*
import static org.hamcrest.Matchers.*

@groovy.transform.TypeChecked
class NginxTest {

    @ClassRule
    public static GenericContainer nginx = new GenericContainer(
        // Ask Testcontainers to build an image for us
        new ImageFromDockerfile()
            .withFileFromClasspath("default.conf", "default.conf")
            .withFileFromClasspath("Dockerfile", "Dockerfile")
    ).withExposedPorts(80)

    @Before
    void setup() {
        RestAssured.baseURI = "http://${nginx.containerIpAddress}:${nginx.firstMappedPort}"
    }

    @Test
    void "check welcome message"() {
        when()
            .get("/")
        .then()
            .statusCode(200)
            .body(containsString("Welcome to nginx!"))
    }

    @Test
    void "check redirect"() {
        given()
            .redirects().follow(false)
        .when()
            .get("/en/blog")
        .then()
            .statusCode(301)
            .header("Location", "http://en.example.com/blog")
    }
}

// Boilerplate
def junit = new org.junit.runner.JUnitCore()
junit.addListener(new org.junit.internal.TextListener(System.out))
def result = junit.run(NginxTest)

System.exit(result.wasSuccessful() ? 0 : 1)