/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component;

import java.io.Serializable;

import net.qoopo.engine.core.entity.Entity;

/**
 * Un componenete es una funcionalidad o módulo que se puede agregar a una
 * entidad, por ejemplo toda entidad tiene un componenete de transformación
 * donde se almacena la información de transofrmación de la entidad.
 * 
 * ejemplos:
 * 
 * Luz
 * Animación
 * Esqueleto
 * Controladores
 * 
 * @author alberto
 */
public abstract class EntityComponent implements Serializable {

    public Entity entity;

    public abstract void destruir();

}
