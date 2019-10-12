<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <!--<xsl:output method="html" indent="yes" encoding="UTF-8"/>-->
    <xsl:template match="/">
        <html>
            <head>
                <style>
                    td {
                    color: white;
                    padding: 1em;
                    }
                    tr {
                    background-color: black;
                    }

                    .action {
                    background-color: red;
                    }
                    .animation {
                    background-color: orange;
                    }
                    .adventure {
                    background-color: blue;
                    }
                </style>
            </head>
            <body>
                <h1>Movies playing this week</h1>
                <table border="1">
                    <xsl:for-each select="movies/movie">
                        <xsl:if test="contains(date-play, 'November')">
                            <tr>
                                <xsl:attribute name="class">
                                    <xsl:value-of
                                            select="translate(genre, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')"/>
                                </xsl:attribute>
                                <td>
                                    <xsl:value-of select="title"/>
                                </td>
                                <td>
                                    <xsl:value-of select="actors"/>
                                </td>
                                <td>
                                    <xsl:value-of select="genre"/>
                                </td>
                                <td>
                                    <xsl:value-of select="date-play"/>
                                </td>
                                <td>
                                    <xsl:value-of select="duration"/>
                                </td>
                            </tr>
                        </xsl:if>

                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>
