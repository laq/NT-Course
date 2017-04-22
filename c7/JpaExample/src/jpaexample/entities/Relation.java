/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpaexample.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author laq
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Relation.findAll", query = "SELECT r FROM Relation r")
    , @NamedQuery(name = "Relation.findById", query = "SELECT r FROM Relation r WHERE r.id = :id")})
public class Relation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @JoinColumn(name = "FRIEND2", referencedColumnName = "ID")
    @ManyToOne
    private Person friend2;
    @JoinColumn(name = "FRIEND1", referencedColumnName = "ID")
    @ManyToOne
    private Person friend1;

    public Relation() {
    }

    public Relation(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Person getFriend2() {
        return friend2;
    }

    public void setFriend2(Person friend2) {
        this.friend2 = friend2;
    }

    public Person getFriend1() {
        return friend1;
    }

    public void setFriend1(Person friend1) {
        this.friend1 = friend1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Relation)) {
            return false;
        }
        Relation other = (Relation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpaexample.entities.Relation[ id=" + id + " ]";
    }
    
}
