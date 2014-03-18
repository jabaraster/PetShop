package com.jabaraster.petshop.entity;

import jabara.jpa.entity.EntityBase_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-03-19T01:27:32.771+0900")
@StaticMetamodel(EPetImageData.class)
public class EPetImageData_ extends EntityBase_ {
	public static volatile SingularAttribute<EPetImageData, EPet> pet;
	public static volatile SingularAttribute<EPetImageData, byte[]> data;
	public static volatile SingularAttribute<EPetImageData, String> contentType;
	public static volatile SingularAttribute<EPetImageData, String> hash;
}
