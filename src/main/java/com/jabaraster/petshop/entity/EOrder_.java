package com.jabaraster.petshop.entity;

import jabara.jpa.entity.EntityBase_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-03-19T23:36:36.294+0900")
@StaticMetamodel(EOrder.class)
public class EOrder_ extends EntityBase_ {
	public static volatile SingularAttribute<EOrder, Integer> quantity;
	public static volatile SingularAttribute<EOrder, EPet> pet;
	public static volatile SingularAttribute<EOrder, EUser> user;
}
