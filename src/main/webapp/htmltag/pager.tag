<ul class="pagination">
    @linkfirst = url + "?rows=" + _PAGE.pageSize + "&page=1";
    @linklast = url + "?rows=" + _PAGE.pageSize + "&page=" + _PAGE.totalPage;  
    <li><a href="${linkfirst}" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
    @var i = 0;
    @while(i < _PAGE.totalPage){
        @i = i + 1;
        @link = url + "?rows=" + _PAGE.pageSize + "&page=" + i;         
        <li ${_PAGE.pageNumber == i ? "class=\"active\"":""}><a href="${link}">${i} <span class="sr-only">(current)</span></a></li>
    @}
    <li><a href="${linklast}" aria-label="Previous"><span aria-hidden="true">&raquo;</span></a></li>
    <li><span>每页${_PAGE.pageSize!}条 共${_PAGE.totalRow}笔数据</span></li>
</ul>
