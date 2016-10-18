package iexls.converter

import iexls.writer.Comment

/**
 * Created by icarokume on 26/09/16.
 */
interface Convertible {

    Map mapHeader()

    Map<String, Comment> mapCommet()

}