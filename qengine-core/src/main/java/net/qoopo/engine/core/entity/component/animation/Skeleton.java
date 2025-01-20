/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.animation;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.math.Matrix4;

/**
 *
 * @author alberto
 */
@Getter
@Setter
public class Skeleton implements EntityComponent {

    @Getter
    @Setter
    private Entity entity;

    // private Entity huesoRaiz;
    private List<Bone> bones = new ArrayList<>();
    private boolean mostrar = false;
    private boolean superponer = false;

    public Skeleton() {
    }

    public Skeleton(List<Bone> huesos) {
        this.bones = huesos;
    }

    public void addBone(Bone hueso) {
        bones.add(hueso);
    }

    public Bone getBone(int i) {
        for (Bone hueso : bones) {
            if (hueso.getIndice() == i) {
                return hueso;
            }
        }
        // System.out.println("Hueso no encontrado (i)" + i);
        return null;
    }

    public Bone getBone(String nombre) {
        for (Bone hueso : bones) {
            // los nombres en este motor siempre se agrea un espacio con un número
            if (hueso.getName().equals(nombre) || hueso.getName().startsWith(nombre + " ")) {
                return hueso;
            }
        }
        // System.out.println("Hueso no encontrado (n) " + nombre);
        return null;
    }

    @Override
    public void destroy() {
        for (Bone hueso : bones) {
            hueso.destruir();
        }
        bones.clear();
        bones = null;
    }

    /**
     * Debe ser llamado una sola vez despuesde la creaci[on de los esqueletos.
     * Calcula las inversas de las trasnformaciones de cada hueso para deshacer
     * la trasnformacion de los vertices y aplicar la trasnformacion de cada
     * animacion
     */
    public void calcularMatricesInversas() {
        // System.out.println("Calculando inversas ");
        for (Bone hueso : bones) {
            // si es el hueso raiz
            if (hueso.getParent() == null) {
                // System.out.println("Hueso raiz=" + hueso.getNombre());
                hueso.calcularTransformacionInversa(new Matrix4());
                // return;
            }
        }
        // System.out.println("No se encontr[o hueso raiz");
    }

    public boolean isMostrar() {
        return mostrar;
    }

    public void setMostrar(boolean mostrar) {
        this.mostrar = mostrar;
    }

    public boolean isSuperponer() {
        return superponer;
    }

    public void setSuperponer(boolean superponer) {
        this.superponer = superponer;
    }

}
