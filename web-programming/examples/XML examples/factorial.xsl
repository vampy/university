<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


<xsl:template name="factorial">
<!--	<xsl:param name="n" select="5"/>     -->
	<xsl:param name="n" />
	<xsl:variable name="sum">
		<xsl:if test="$n = 1"> 1 </xsl:if>
		<xsl:if test="$n != 1">
			<xsl:call-template name="factorial">
				<xsl:with-param name="n" select="$n - 1"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:variable>
	<xsl:value-of select="$sum * $n"/>
</xsl:template>


<xsl:template match="/">
<html>
<body>
<xsl:for-each select="array/number">
	<p>Factorial <xsl:value-of select="." />:
	<xsl:call-template name="factorial">
		<xsl:with-param name="n" select="." />
	</xsl:call-template>
	</p>
</xsl:for-each>
</body>
</html>
</xsl:template>

</xsl:stylesheet>