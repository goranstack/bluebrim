CoCompositeContentClientConstant was moved out from the project CompositeContentClient
to awoid circular dependeny between CompositeContentClient and LayoutClient since
CompositeContent is dependent on LayoutContent and LayoutContent reference
CoCompositeContentClientConstants.