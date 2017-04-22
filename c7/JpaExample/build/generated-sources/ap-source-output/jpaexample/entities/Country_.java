package jpaexample.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpaexample.entities.Person;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-04-21T01:05:18")
@StaticMetamodel(Country.class)
public class Country_ { 

    public static volatile CollectionAttribute<Country, Person> personCollection;
    public static volatile SingularAttribute<Country, String> name;
    public static volatile SingularAttribute<Country, Integer> id;

}