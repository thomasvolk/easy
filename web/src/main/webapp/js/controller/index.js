

IndexController = function(pageId) {

    self = this

    self.page = new easy.Page(pageId);
    document.title = pageId
    $('#title').text(pageId)

    self.page.onContentReady(function(data) {
        $('#editor').html(data.content);
        $("#editor").jqte({
            change: function(){
                self.page.save($('.jqte_editor').html());
            }
         });
        $('#status').click( function() { self.page.forceSave($('.jqte_editor').html()); } );

        for(var i in data.subPages) {
          $('<li><a class="subpagelink" href="/easy?p=' + data.subPages[i][0] + '" data-ajax="false" data-role="button">' + data.subPages[i][1] + '</a></li>').insertBefore('#list_navigation_back');
        }

        if(data.parentPage[0] !== '') {
          $('#list_navigation_parent a').attr("href", '/easy?p=' + data.parentPage[0]);
          $('#list_navigation_parent').show();
        }
    } );

    self.page.onStatusChanged(function(status) {
        $('#status span').hide();
        if(status == 'SAVED') {
            $('#status .saved').show();
            $('#status').buttonMarkup({ icon: 'check' });
        }
        else if(status == 'CHANGED') {
            $('#status .changed').show();
            $('#status').buttonMarkup({ icon: 'star' });
        }
        else if(status == 'WAIT') {
            $('#status .wait').show();
            $('#status').buttonMarkup({ icon: 'gear' });
        }
        else {
            $('#status .unknown').show();
            $('#status').buttonMarkup({ icon: 'alert' });
        }
    } );

    self.page.load();
    $('#list_navigation_parent').hide();

};


