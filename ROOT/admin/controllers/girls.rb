FindGirls::Admin.controllers :girls do
  get :index do
    @title = "Girls"
    @girls = Girls.all
    render 'girls/index'
  end

  get :new do
    @title = pat(:new_title, :model => 'girls')
    @girls = Girls.new
    render 'girls/new'
  end

  post :create do
    @girls = Girls.new(params[:girls])
    if @girls.save
      @title = pat(:create_title, :model => "girls #{@girls.id}")
      flash[:success] = pat(:create_success, :model => 'Girls')
      params[:save_and_continue] ? redirect(url(:girls, :index)) : redirect(url(:girls, :edit, :id => @girls.id))
    else
      @title = pat(:create_title, :model => 'girls')
      flash.now[:error] = pat(:create_error, :model => 'girls')
      render 'girls/new'
    end
  end

  get :edit, :with => :id do
    @title = pat(:edit_title, :model => "girls #{params[:id]}")
    @girls = Girls.find(params[:id])
    if @girls
      render 'girls/edit'
    else
      flash[:warning] = pat(:create_error, :model => 'girls', :id => "#{params[:id]}")
      halt 404
    end
  end

  put :update, :with => :id do
    @title = pat(:update_title, :model => "girls #{params[:id]}")
    @girls = Girls.find(params[:id])
    if @girls
      if @girls.update_attributes(params[:girls])
        flash[:success] = pat(:update_success, :model => 'Girls', :id =>  "#{params[:id]}")
        params[:save_and_continue] ?
          redirect(url(:girls, :index)) :
          redirect(url(:girls, :edit, :id => @girls.id))
      else
        flash.now[:error] = pat(:update_error, :model => 'girls')
        render 'girls/edit'
      end
    else
      flash[:warning] = pat(:update_warning, :model => 'girls', :id => "#{params[:id]}")
      halt 404
    end
  end

  delete :destroy, :with => :id do
    @title = "Girls"
    girls = Girls.find(params[:id])
    if girls
      if girls.destroy
        flash[:success] = pat(:delete_success, :model => 'Girls', :id => "#{params[:id]}")
      else
        flash[:error] = pat(:delete_error, :model => 'girls')
      end
      redirect url(:girls, :index)
    else
      flash[:warning] = pat(:delete_warning, :model => 'girls', :id => "#{params[:id]}")
      halt 404
    end
  end

  delete :destroy_many do
    @title = "Girls"
    unless params[:girls_ids]
      flash[:error] = pat(:destroy_many_error, :model => 'girls')
      redirect(url(:girls, :index))
    end
    ids = params[:girls_ids].split(',').map(&:strip)
    girls = Girls.find(ids)
    
    if Girls.destroy girls
    
      flash[:success] = pat(:destroy_many_success, :model => 'Girls', :ids => "#{ids.to_sentence}")
    end
    redirect url(:girls, :index)
  end
end
