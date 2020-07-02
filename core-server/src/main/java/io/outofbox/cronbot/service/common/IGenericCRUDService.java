package io.outofbox.cronbot.service.common;

import io.outofbox.cronbot.error.ConflictExcpetion;
import io.outofbox.cronbot.error.NotFoundException;
import io.outofbox.cronbot.error.OperationFailureException;
import io.outofbox.cronbot.model.plugin.Plugin;
import io.outofbox.cronbot.model.plugin.PluginDetails;

import java.util.List;

public interface IGenericCRUDService<Obj, Details> {

    Obj findById(String id) throws NotFoundException, OperationFailureException;

    List<Obj> findAllWithPage(int page, int size) throws OperationFailureException;

    Obj create(Details details) throws OperationFailureException, ConflictExcpetion;

    Obj update(String id, Details details) throws OperationFailureException, NotFoundException;

    Obj delete(String id) throws OperationFailureException,NotFoundException;
}
