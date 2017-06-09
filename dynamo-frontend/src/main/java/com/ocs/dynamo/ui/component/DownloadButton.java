/*
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.ocs.dynamo.ui.component;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Button;

import java.io.InputStream;

/**
 * A button that starts a file download process when clicked
 * 
 * @author bas.rutten
 */
public abstract class DownloadButton extends Button {

    private static final long serialVersionUID = -7163648327567831406L;

    private StreamResource resource;

    /**
     * Constructor
     * 
     * @param caption
     *            the caption of the button
     */
    public DownloadButton(String caption) {
        super(caption);
        resource = new StreamResource(new StreamSource() {

            private static final long serialVersionUID = -4870779918745663459L;

            @Override
            public InputStream getStream() {
                return doCreateContent();
            }

        }, doCreateFileName());

        FileDownloader downloader = new FileDownloader(resource);
        downloader.extend(this);

    }

    /**
     * Creates the file content (as a byte array)
     * 
     * @return
     */
    protected abstract InputStream doCreateContent();

    /**
     * Creates the file name
     * 
     * @return
     */
    protected abstract String doCreateFileName();

    /**
     * Sets the file name
     * 
     * @param fileName
     */
    public void setFileName(String fileName) {
        resource.setFilename(fileName);
    }

}
