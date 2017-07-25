@GrabResolver(name='jitpack', root='https://jitpack.io', m2Compatible='true')
@Grab('com.github.testcontainers:testcontainers-groovy-script:1.4.2')
@groovy.transform.BaseScript(TestcontainersScript)
import org.testcontainers.containers.*
import org.junit.*

@Grab('io.rest-assured:rest-assured:3.0.3')
@GrabExclude("org.codehaus.groovy:groovy-xml")
import io.restassured.*
import static io.restassured.RestAssured.*
import static org.hamcrest.Matchers.*

@groovy.transform.Field
@ClassRule
static public GenericContainer nginx = new GenericContainer("nginx:1.9.4")
    .withClasspathResourceMapping("default.conf", "/etc/nginx/conf.d/default.conf", BindMode.READ_ONLY)
    .withExposedPorts(80)

@BeforeClass
static void setup() {
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