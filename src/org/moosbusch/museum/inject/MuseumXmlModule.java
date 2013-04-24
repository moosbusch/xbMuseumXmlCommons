/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.moosbusch.museum.inject;

import com.google.inject.Module;

/**
 *
 * @author moosbusch
 */
public interface MuseumXmlModule extends Module {

    public String getLanguage();
    
}
