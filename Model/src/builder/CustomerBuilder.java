package builder;

import entity.Customer;
import jaxb.generated.SDMCustomer;

import javax.xml.bind.ValidationException;

public class CustomerBuilder implements Builder<SDMCustomer, Customer>{
    @Override
    public Customer build(SDMCustomer source) {
        return new Customer(
                source.getId(),
                source.getName(),
                new PointBuilder().build(source.getLocation())
        );
    }
}
