

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
    } );

    self.page.onStatusChanged(function(status) {
        var bar = $('#statusBar');
        bar.removeClass('saved').removeClass('changed').removeClass('wait');
        $('#status span').hide();
        if(status == 'SAVED') {
            $('#status .saved').show();
        }
        else if(status == 'CHANGED') {
            bar.addClass('changed');
            $('#status .changed').show();
        }
        else if(status == 'WAIT') {
            bar.addClass('wait');
            $('#status .wait').show();

        }
    } );

    self.page.load();

    self.subpages.onContentReady(function(content) {
       for(var i in content) {
         $('<li><a class="subpagelink" href="/?p=' + content[i].id + '" data-ajax="false" data-role="button">' + content[i].title + '</a></li>').insertBefore('#list_navigation_back');
       }
    } );
    self.subpages.load();

    $('#list_navigation_parent').hide();
    self.parentPage.onContentReady(function(content) {
        if(typeof(content.id) !== 'undefined') {
          $('#list_navigation_parent a').click(function(a) {  window.location.href = '/?p=' + content.id; } );
          $('#list_navigation_parent').show();
        }
    } );
    self.parentPage.load();

};


