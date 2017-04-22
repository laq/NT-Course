package jpaexample.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpaexample.entities.Person;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-04-21T01:05:18")
@StaticMetamodel(Relation.class)
public class Relation_ { 

    public static volatile SingularAttribute<Relation, Person> friend1;
    public static volatile SingularAttribute<Relation, Integer> id;
    public static volatile SingularAttribute<Relation, Person> friend2;

}