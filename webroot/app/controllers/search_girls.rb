FindGirls::App.controllers :search_girls do
  
  # get :index, :map => '/foo/bar' do
  #   session[:foo] = 'bar'
  #   render 'index'
  # end

  # get :sample, :map => '/sample/url', :provides => [:any, :js] do
  #   case content_type
  #     when :js then ...
  #     else ...
  # end

  # get :foo, :with => :id do
  #   'Maps to url '/foo/#{params[:id]}''
  # end

  # get '/example' do
  #   'Hello world!'
  # end


  get '/list', :with => :id, :cache => true do
    Oj.dump({'result' => 'FindGirls::App.controllers list'})

    $redis.with do |client|
      @last_user_id = client.get('aaa')
    end

    doc1 = {'result' => 'FindGirls::App.controllers list'}
    doc = Oj::StringWriter.new(nil)
    doc.push_value(doc1, 'aaaa')
    doc.push_value(params[:id], "id")
    Oj.dump(doc.to_s)

    render "search_girls/girlslist.html"
  end
end
