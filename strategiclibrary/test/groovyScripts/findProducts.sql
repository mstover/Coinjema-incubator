#if(!$table)
	select * from ia_bali where 1=0
#elseif($category)
	select * from findProducts('$!table','$!category','$!value')
#else
	select * from findAllProducts('$!table')
#end