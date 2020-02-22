/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calendar;

/**
 *
 * @author April
 */
public class NotFoundException extends RuntimeException{
    public NotFoundException(String s){
        super(s);
    }
}