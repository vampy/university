<?xml version="1.0"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">

<html>
	<body>
		<h2>A Book Collection</h2>
		<table border="1">
			<xsl:for-each select="collection/book">
			<tr>
			      <td><xsl:value-of select="title"/></td>
		              <td><xsl:value-of select="author"/></td>
	       	              <td><xsl:value-of select="isbn"/></td>
	                      <td><xsl:value-of select="editor"/></td>
			</tr>
			</xsl:for-each>
		</table>
	</body>
</html>

</xsl:template>
</xsl:stylesheet>
