package au.org.ala.delta.translation;

import java.util.logging.Logger;

import au.org.ala.delta.DeltaContext;
import au.org.ala.delta.model.Attribute;
import au.org.ala.delta.model.DeltaDataSet;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.translation.attribute.AttributeParser;
import au.org.ala.delta.translation.attribute.ParsedAttribute;
import au.org.ala.delta.translation.attribute.ParsedAttribute.CommentedValues;

/**
 * The DataSetTranslator iterates through the Items and Attributes of a DeltaDataSet, raising
 * events for translator classes to handle.
 */
public abstract class AbstractDataSetTranslator implements DataSetTranslator {

	private static Logger logger = Logger.getLogger(AbstractDataSetTranslator.class.getName());
	
	private DeltaContext _context;
	private DataSetFilter _filter;
	private AttributeParser _parser;
	
	public AbstractDataSetTranslator(DeltaContext context, DataSetFilter filter) {
		_context = context;;
		_filter = filter;
		_parser = new AttributeParser();
	}
	
	public void translate() {
		
		DeltaDataSet dataSet = _context.getDataSet();
		
		beforeFirstItem();
		
		int numItems = dataSet.getMaximumNumberOfItems();
		for (int i=1; i<=numItems; i++) {
			
			Item item = dataSet.getItem(i);
			
			if (_filter.filter(item)) {
				beforeItem(item);
				translateItem(item);	
				afterItem(item);
			}	
		}
		
		afterLastItem();
	}
	
	/**
	 * Iterates through the attributes of an Item.
	 * @param item the item to translate.
	 */
	private void translateItem(Item item) {
		
		DeltaDataSet dataSet = _context.getDataSet();
		
		int numChars = dataSet.getNumberOfCharacters();
		for (int i=1; i<=numChars; i++) {
			
			au.org.ala.delta.model.Character character = dataSet.getCharacter(i);
			Attribute attribute = item.getAttribute(character);
			
			if (_filter.filter(item, character)) {
				logger.fine(item.getItemNumber() + ", "+character.getCharacterId()+" = "+attribute.getValue());
				beforeAttribute(attribute);
				translateAttribute(attribute);
				afterAttribute(attribute);
			}	
		}
	}
	
	private void translateAttribute(Attribute attribute) {
		String value = attribute.getValue();
		ParsedAttribute parsedAttribute = _parser.parse(value);
		
		attributeComment(parsedAttribute.getCharacterComment());
		
		for (CommentedValues values : parsedAttribute.getCommentedValues()) {
			attributeValues(values.getValues());
			attributeComment(values.getComment());
		}
	}
}