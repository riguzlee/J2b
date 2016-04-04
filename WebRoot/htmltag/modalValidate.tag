<div>
    <div class="modal fade" id="${id}" tabindex="-1" role="dialog" aria-labelledby="${id}-label">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title" id="${id}-label">${title!}</h4>
                </div>
                <div class="modal-body">${content!}</div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary btn-sm pull-left" onclick='validateAndSubmit("#${formId}")'>
                        <span class="glyphicon glyphicon-ok">保存</span>
                    </button>
                    <button type="button" class="btn btn-inverse btn-sm pull-right" data-dismiss="modal">
                         <span class="glyphicon glyphicon-remove">取消</span>
                     </button>
                </div>
            </div>
        </div>
    </div>    
</div>
