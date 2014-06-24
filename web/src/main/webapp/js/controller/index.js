

IndexController = function(pageId) {

    self = this

    self.page = new easy.Page(pageId);
    self.subpages = new easy.Subpages(pageId);
    self.parentPage = new easy.ParentPage(pageId)
    document.title = pageId
    $('#title').text(pageId)

    self.page.onContentReady(function(content) {
        $('#editor').html(content);
        $("#editor").jqte({
            change: function(){
                self.page.save($('.jqte_editor').html());
            }
         });
        $('#status').click( function() { self.page.forceSave($('.jqte_editor').html()); } );
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

    self.subpages.onContentReady(function(content) {
       for(var i in content) {
         $('<li><a class="subpagelink" href="/easy?p=' + content[i].id + '" data-ajax="false" data-role="button">' + content[i].title + '</a></li>').insertBefore('#list_navigation_back');
       }
    } );
    self.subpages.load();

    $('#list_navigation_parent').hide();
    self.parentPage.onContentReady(function(content) {
        if(typeof(content.id) !== 'undefined') {
          $('#list_navigation_parent a').attr("href", '/easy?p=' + content.id);
          $('#list_navigation_parent').show();
        }
    } );
    self.parentPage.load();

};


