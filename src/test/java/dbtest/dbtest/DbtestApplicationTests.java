package dbtest.dbtest;

import java.net.URI;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = DbtestApplicationTests.Initializer.class)
class DbtestApplicationTests {
	private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(DbtestApplicationTests.class.getName());

    private static final MSSQLServerContainer mssqlserver = new MSSQLServerContainer(DockerImageName.parse("mcr.microsoft.com/mssql/server:2019-latest"))
        .acceptLicense();

	static {
		mssqlserver.start();
	}

	static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(
                final ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                            "spring.datasource.url=" + mssqlserver.getJdbcUrl(),
                            "spring.datasource.username=" + mssqlserver.getUsername(),
                            "spring.datasource.password=" + mssqlserver.getPassword())
                    .applyTo(configurableApplicationContext.getEnvironment());
        }
    }

	@LocalServerPort
    int randomServerPort;

	@Test
	void contextLoads() {
	    String url = mssqlserver.getJdbcUrl();
		log.info(url);
		Assertions.assertNotNull(url);
	}

	@Test
	public void testMockEndpoint() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
     
 		final String baseUrl = "http://localhost:" + randomServerPort + "/mock-test";
 		URI uri = new URI(baseUrl);
 
 		ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
		Assertions.assertEquals(200, result.getStatusCodeValue());
	}

	@Test
	public void testDBEndpoint() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
     
 		final String baseUrl = "http://localhost:" + randomServerPort + "/db-test";
 		URI uri = new URI(baseUrl);
 
 		ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
		Assertions.assertEquals(200, result.getStatusCodeValue());
	}

	@Test
	public void testIncorrectEndpoint() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
     
 		final String baseUrl = "http://localhost:" + randomServerPort + "/q-test";
 		URI uri = new URI(baseUrl);
 
		Assertions.assertThrows(org.springframework.web.client.HttpClientErrorException.class, () -> {
			restTemplate.getForEntity(uri, String.class);
		});
	}
}

