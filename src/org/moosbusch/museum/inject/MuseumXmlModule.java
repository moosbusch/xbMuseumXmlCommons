/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.moosbusch.museum.inject;

import com.google.inject.Module;
import com.google.inject.Provides;
import org.moosbusch.museum.document.MuseumXmlDocument;

/**
 *
 * @author moosbusch
 */
public interface MuseumXmlModule extends Module {

    @Provides
    public MuseumXmlDocument<?, ?, ? ,?> createDocument();

    public String getLanguage();

}
