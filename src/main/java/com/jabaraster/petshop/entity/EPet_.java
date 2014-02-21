package com.jabaraster.petshop.entity;

import jabara.jpa.entity.EntityBase_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-02-21T01:26:06.779+0900")
@StaticMetamodel(EPet.class)
public class EPet_ extends EntityBase_ {
	public static volatile SingularAttribute<EPet, String> name;
	public static volatile SingularAttribute<EPet, EPetCategory> category;
	public static volatile SingularAttribute<EPet, Integer> unitPrice;
}
