/**
 * 
 */
package com.jabaraster.petshop.entity;

import jabara.jpa.entity.EntityBase;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author jabaraster
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ECart extends EntityBase<ECart> {
    private static final long serialVersionUID = 94486105819285325L;

    /**
     * 
     */
    @Getter
    @Setter
    @JoinColumn(nullable = false)
    protected EUser           user;

    /**
     * 
     */
    @Getter
    @OneToMany
    protected List<EOrder>    orders           = new ArrayList<>();
}
