package org.celstec.arlearn2.portal.client.search;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.OwnerResponseDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.QueryGameDataSource;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class SearchForm extends DynamicForm {
	
	private TextItem query;
	
	public SearchForm() {
		super();
		
		setPadding(10);
		//setWidth("500px");
		setBackgroundColor("#F1F1F1");
		setDataSource(OwnerResponseDataSource.getInstance());
		setAutoFocus(false);
		
		createTextItem();
		
		setItems(query);
	}

	private void createTextItem() {
		// TODO Auto-generated method stub
		query = new TextItem("query");
        query.setShowFocused(false);
        query.setCanFocus(false);
        query.setShowTitle(false);
        query.setWidth("500px");
        
        /**
         * DEPRECATED 
         * 
         * Difference between use this instead current approach, is that in this case 
         * search happens every time user press a button. This could be inefficient.
         * */
//        query.addChangedHandler(new ChangedHandler() {
//			
//			@Override
//			public void onChanged(ChangedEvent event) {
//    			//getGrid().setData(getGridData(listRecords));
//				
//			}
//		});
	}

	public TextItem getQuery() {
		return query;
	}

	public void setQuery(TextItem query) {
		this.query = query;
	}

	
	
	
}
