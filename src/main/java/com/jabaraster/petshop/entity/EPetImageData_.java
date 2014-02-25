package com.jabaraster.petshop.entity;

import jabara.jpa.entity.EntityBase_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-02-25T22:35:56.340+0900")
@StaticMetamodel(EPetImageData.class)
public class EPetImageData_ extends EntityBase_ {
	public static volatile SingularAttribute<EPetImageData, byte[]> data;
	public static volatile SingularAttribute<EPetImageData, EPet> pet;
	public static volatile SingularAttribute<EPetImageData, String> contentType;
}
