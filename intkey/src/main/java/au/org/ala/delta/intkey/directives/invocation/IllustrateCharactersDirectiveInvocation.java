/*******************************************************************************
 * Copyright (C) 2011 Atlas of Living Australia
 * All Rights Reserved.
 *   
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *   
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 ******************************************************************************/
package au.org.ala.delta.intkey.directives.invocation;

import java.util.ArrayList;
import java.util.List;

import au.org.ala.delta.intkey.model.IntkeyContext;
import au.org.ala.delta.model.Character;

public class IllustrateCharactersDirectiveInvocation extends BasicIntkeyDirectiveInvocation {

    private List<au.org.ala.delta.model.Character> _characters;
    
    public void setCharacters(List<au.org.ala.delta.model.Character> characters) {
        this._characters = characters;
    }

    @Override
    public boolean execute(IntkeyContext context) {
        //filter out any taxa that do not have images
        List<Character> charsWithoutImages = new ArrayList<Character>();
        for (Character ch : _characters) {
            if (ch.getImageCount() == 0) {
                charsWithoutImages.add(ch);
            }
        }
        
        _characters.removeAll(charsWithoutImages);
        
        if (_characters.isEmpty()) {
            context.getUI().displayErrorMessage("No images for the specified characters");
            return false;
        }
        
        context.getUI().illustrateCharacters(_characters);
        return true;
    }

}
