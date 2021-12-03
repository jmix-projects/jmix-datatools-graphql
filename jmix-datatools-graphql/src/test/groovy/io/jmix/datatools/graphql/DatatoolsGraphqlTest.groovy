package io.jmix.datatools.graphql

import com.graphql.spring.boot.test.GraphQLResponse
import io.jmix.core.UnconstrainedDataManager
import org.springframework.beans.factory.annotation.Autowired
import test_support.entity.Customer

class DatatoolsGraphqlTest extends AbstractGraphQLTest {
    private static final UUID ID = UUID.fromString("bb7b5923-5b88-4dd7-aad9-9d2f735009dd")

    @Autowired
    private UnconstrainedDataManager dataManager

    def "Entity restore endpoint works"() {
        when:
        Customer customer = dataManager.create(Customer)
        customer.id = ID
        customer.name = "Ivan"
        customer = dataManager.save(customer)
        dataManager.remove(customer)

        GraphQLResponse response = query("test/restoreEntities.graphql")

        Optional<Customer> loaded = dataManager.load(Customer).id(customer.id).optional()

        then:
        response.getRawResponse().getBody() == '{"data":{"restoreEntities":1}}'
        loaded.isPresent()

    }

}


