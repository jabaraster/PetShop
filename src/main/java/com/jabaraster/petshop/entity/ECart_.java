package com.jabaraster.petshop.entity;

import jabara.jpa.entity.EntityBase_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-02-26T07:08:08.770+0900")
@StaticMetamodel(ECart.class)
public class ECart_ extends EntityBase_ {
	public static volatile SingularAttribute<ECart, EUser> user;
	public static volatile ListAttribute<ECart, EOrder> orders;
}
